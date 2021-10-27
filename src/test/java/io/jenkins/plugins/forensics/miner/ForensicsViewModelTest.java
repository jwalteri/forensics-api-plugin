package io.jenkins.plugins.forensics.miner;

import java.util.HashSet;

import javax.mail.Message;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.plugins.MockMaker.StaticMockControl;

import edu.hm.hafner.echarts.JacksonFacade;
import edu.hm.hafner.echarts.PieChartModel;

import hudson.model.Run;

import io.jenkins.plugins.forensics.util.CommitDecoratorFactory;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ForensicsViewModelTest {
    private static final String SCM_KEY = "scmKey";

    @Test
    void getOwnerTest() {
        RepositoryStatistics repositoryStatisticsStub = mock(RepositoryStatistics.class);
        Run<?, ?> ownerStub = mock(Run.class);
        final String expected = "owner";
        when(ownerStub.getDisplayName()).thenReturn(expected);

        ForensicsViewModel sut = new ForensicsViewModel(ownerStub, repositoryStatisticsStub, SCM_KEY);

        assertThat(sut.getOwner().getClass()).isEqualTo(ownerStub.getClass());
        assertThat(sut.getOwner().getDisplayName()).isEqualTo(sut.getOwner().getDisplayName());
    }

    @Test
    void getDisplayNameTest() {
        RepositoryStatistics repositoryStatisticsStub = mock(RepositoryStatistics.class);
        Run<?, ?> ownerStub = mock(Run.class);
        final String expected = "Forensics_Action";
        MockedStatic<Messages> messagesMock = mockStatic(Messages.class);
        messagesMock.when(Messages::Forensics_Action).thenReturn(expected);

        ForensicsViewModel sut = new ForensicsViewModel(ownerStub, repositoryStatisticsStub, SCM_KEY);

        assertThat(sut.getDisplayName().getClass()).isEqualTo(expected.getClass());
        assertThat(sut.getDisplayName()).isEqualTo(expected);
        messagesMock.close();
    }

    @Test
    void getAuthorsModelTest() {
        RepositoryStatistics repositoryStatisticsStub = mock(RepositoryStatistics.class);
        Run<?, ?> ownerStub = mock(Run.class);
        SizePieChart sizePieChartMock = mock(SizePieChart.class);
        final String expected = new JacksonFacade().toJson(new PieChartModel());
        when(sizePieChartMock.create(any(RepositoryStatistics.class),
                any())).thenReturn(new PieChartModel());

        ForensicsViewModel sut = new ForensicsViewModel(ownerStub, repositoryStatisticsStub, SCM_KEY);

        assertThat(sut.getAuthorsModel()).isEqualTo(expected);
    }

    @Test
    void getScmKeyTest() {
        RepositoryStatistics repositoryStatisticsStub = mock(RepositoryStatistics.class);
        Run<?, ?> ownerStub = mock(Run.class);
        final String expected = "scmKey";
        ForensicsViewModel sut = new ForensicsViewModel(ownerStub, repositoryStatisticsStub, expected);

        assertEquals(expected, sut.getScmKey());
    }

    @Test
    void getTableModelTest() {
        RepositoryStatistics repositoryStatisticsStub = mock(RepositoryStatistics.class);
        Run<?, ?> ownerStub = mock(Run.class);
        ForensicsViewModel sut = new ForensicsViewModel(ownerStub, repositoryStatisticsStub, SCM_KEY);

        assertThat(sut.getTableModel(any()).getClass()).isEqualTo(ForensicsTableModel.class);
    }

    @Test
    void getDynamicTest() {
        Run<?, ?> ownerStub = mock(Run.class);
        RepositoryStatistics repositoryStatisticsStub = mock(RepositoryStatistics.class);
        when(repositoryStatisticsStub.getFileStatistics()).thenAnswer(x -> { throw new IllegalArgumentException(); });
        //when(repositoryStatisticsStub.getFileStatistics()).thenReturn(new HashSet<>());

        //MockedStatic<CommitDecoratorFactory> commitDecoratorFactoryMock = mockStatic(CommitDecoratorFactory.class);
        mockStatic(CommitDecoratorFactory.class);
        when(CommitDecoratorFactory.findCommitDecorator(ownerStub)).thenReturn(any());

        ForensicsViewModel sut = new ForensicsViewModel(ownerStub, repositoryStatisticsStub, SCM_KEY);

        assertThat(sut.getDynamic(any(), any(), any()).getClass()).isEqualTo(FileDetailsView.class);




    }
}